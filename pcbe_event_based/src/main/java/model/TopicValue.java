package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import companies.Company;

import java.io.Serializable;

@XStreamAlias("TopicValue")
public class TopicValue implements Serializable {

    private static final long serialVersionUID = -2749977649306134186L;

    public int id;
    public boolean buyer = false;
    public boolean seller = false;
    public Company company;

    public TopicValue(int id, boolean buyer, Company company) {
        this.id = id;
        this.buyer = buyer;
        this.seller = !buyer;
        this.company = company;
    }

    public TopicValue(int id, boolean buyer, boolean seller, Company company) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.company = company;
    }

    @Override
    public String toString() {
        return id + " " + buyer + " " + seller + " " + company;
    }
}
