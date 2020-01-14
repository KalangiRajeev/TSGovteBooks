package com.ikalangirajeev.tsgovtebooks;

public class EbookItem {
    private String paraName;
    private String paraDesc;

    public EbookItem(String paraName, String paraDesc) {
        this.paraName = paraName;
        this.paraDesc = paraDesc;
    }

    public String getParaName() {
        return paraName;
    }

    public String getParaDesc() {
        return paraDesc;
    }

}
