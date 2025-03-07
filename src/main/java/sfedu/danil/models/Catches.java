package sfedu.danil.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


public class Catches {
    private List<Catch> catchList;

    public List<Catch> getCatchList() {
        return catchList;
    }

    public void setCatchList(List<Catch> catchList) {
        this.catchList = catchList;
    }
}
