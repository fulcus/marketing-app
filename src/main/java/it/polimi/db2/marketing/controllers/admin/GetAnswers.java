package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.DateException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import it.polimi.db2.marketing.ejb.services.UserService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@WebServlet("/GetAnswers")
public class GetAnswers extends ServletBase {
    private static final long serialVersionUID = 1L;

    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionnaireService")
    private QuestionnaireService qnnaireService;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/UserService")
    private UserService userService;

    public GetAnswers() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        Date date = null;
        Integer userid = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(request.getParameter("date"));
            userid = Integer.parseInt(request.getParameter("userid"));
            if (getToday().before(date)) {
                throw new DateException("Missing or empty credential value");
            }
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date inserted has wrong format");
        } catch (DateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        // show questionnaire answers of each user
        // get questions and answers of user for questionnaire of date
        List<Question> questions;
        List<String> answers;
        Questionnaire questionnaire;
        User user;

        try {
            questionnaire = qnnaireService.findByDate(date);
            questions = qnnaireService.getQuestions(questionnaire);
            user = userService.getUser(userid);
            answers = qnnaireService.getAnswersToQuestions(user, questions);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("questions", questions);
        variables.put("answers", answers);
        variables.put("user", user);
        renderPage(request, response, "/WEB-INF/answers.html", variables);
    }

    private Date getToday() {
        return new Date(System.currentTimeMillis());
    }
}