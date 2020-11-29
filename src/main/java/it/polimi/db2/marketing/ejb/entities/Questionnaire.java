package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the usertable database table.
 */
@Entity
@Table(name = "questionnaire", schema = "db_marketing")
@NamedQueries({
        @NamedQuery(name = "Questionnaire.getAll", query = "SELECT q FROM Questionnaire q")
})

public class Questionnaire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private String title;

    @OneToMany(mappedBy="questionnaire")
    private Collection<Question> questions;

    public Questionnaire() {
    }

    public Questionnaire(Collection<Question> questions, Date date, String title){
        this.questions = questions;
        this.date = date;
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}