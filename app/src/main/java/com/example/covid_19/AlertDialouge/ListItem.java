package com.example.covid_19.AlertDialouge;

public class ListItem {
    String State;
    String Infected;
    String Recovered;
    String Death;

    public ListItem(String state, String infected, String recovered, String death) {
        State = state;
        Infected = infected;
        Recovered = recovered;
        Death = death;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getInfected() {
        return Infected;
    }

    public void setInfected(String infected) {
        Infected = infected;
    }

    public String getRecovered() {
        return Recovered;
    }

    public void setRecovered(String recovered) {
        Recovered = recovered;
    }

    public String getDeath() {
        return Death;
    }

    public void setDeath(String death) {
        Death = death;
    }
}
