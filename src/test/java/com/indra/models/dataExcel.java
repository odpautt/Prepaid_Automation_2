package com.indra.models;

import com.indra.actions.ReadFileXLSX;

public class dataExcel {
    String urlEpos;
    String urlCRM;
    String urlComfirmador;
    String urlGatewayCBS;
    String urlGatewayMG;
    String MSISDN;
    String Plu;
    String Serial;

    ReadFileXLSX fileXLSX = new ReadFileXLSX();

    public String getUrlEpos() {
        return urlEpos;
    }

    public String getUrlCRM() {
        return urlCRM;
    }

    public String getUrlComfirmador() {
        return urlComfirmador;
    }

    public String getUrlGatewayCBS() {
        fileXLSX.readFileExcel();
        urlGatewayCBS = fileXLSX.excelArray.get(1).get(3);
        return urlGatewayCBS;
    }

    public String getUrlGatewayMG() {
        fileXLSX.readFileExcel();
        urlGatewayCBS = fileXLSX.excelArray.get(1).get(4);
        return urlGatewayMG;
    }

    public String getMSISDN() {
        fileXLSX.readFileExcel();
        MSISDN = fileXLSX.excelArray.get(3).get(2);
        return MSISDN;
    }

    public String getPlu() {
        return Plu;
    }

    public String getSerial() {
        return Serial;
    }

    public ReadFileXLSX getFileXLSX() {
        return fileXLSX;
    }
}
