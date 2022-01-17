package com.indra.actions;

import com.indra.pages.PruebaPages;
import org.openqa.selenium.WebDriver;

import static io.restassured.RestAssured.given;


public class UninstallCBSServices extends PruebaPages {
    public UninstallCBSServices(WebDriver driver) {
        super(driver);
    }

    public void performLineCleaning(String urlGatewayCBS, String urlGatewayMG, String MSISDN){
        System.out.println("CBS  "+urlGatewayCBS);
        System.out.println("MG  "+urlGatewayMG);
        System.out.println("MSISDN  "+MSISDN);
       String response = queryCustomerInfoGatewayCBS(MSISDN, urlGatewayCBS);
        if(validateActiveAccount(response)){
            System.out.println("cliente activo ?? "+validateActiveAccount(response));
            //uninstallSubscriptionGatewayMG(MSISDN,urlGatewayMG);
        }
        if(validateActiveAccount(response) && validatesIfPlanIsDifferentToPrepaid(response)){
            System.out.println("cliente activo ?? "+validateActiveAccount(response));
            System.out.println("cliente con plan diferente a prepago?? "+validatesIfPlanIsDifferentToPrepaid(response));
            String Acctkey = extractResponseInformation(response,"ns3:AcctKey");
            System.out.println(Acctkey);
            //uninstallSubscriptionGatewayMG(MSISDN,urlGatewayMG);
            //acctDeactivationGatewayCBS(Acctkey,urlGatewayCBS);
        }
    }
    /** ejecuta el servicio querycustomerinfo, para porder validar si esta activa la cuenta,
        si es prepago o no y para optener el numero de la cuenta*/
    public String queryCustomerInfoGatewayCBS(String MSISDN, String URL) {

      String response =   given()
                .headers("Content-type","text/xml;charset=UTF-8")
                .body("<soapenv:Envelope xmlns:bcc=\"http://www.huawei.com/bme/cbsinterface/bccommon\" xmlns:bcs=\"http://www.huawei.com/bme/cbsinterface/bcservices\" xmlns:ws=\"http://ws.web.gatewaycbs.tigo.com.co/\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <ws:queryCustomerInfo>\n" +
                        "         <!--Optional:-->\n" +
                        "         <QueryCustomerInfoRequest>\n" +
                        "            <bcs:QueryObj>\n" +
                        "               <!--Optional:-->\n" +
                        "               <bcs:SubAccessCode>\n" +
                        "                  <!--Optional:-->\n" +
                        "                  <bcc:PrimaryIdentity>" +  MSISDN+ "</bcc:PrimaryIdentity>\n" +
                        "               </bcs:SubAccessCode>\n" +
                        "            </bcs:QueryObj>\n" +
                        "         </QueryCustomerInfoRequest>\n" +
                        "      </ws:queryCustomerInfo>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>")
                .post(URL)
                .then().statusCode(200)
                .extract().asString();

        return response;

    }

    /** ejecuta el servicio para desinstalar la subscriocion que tiene la linea */
    public String uninstallSubscriptionGatewayMG(String MSISDN, String URL){
        String response = given()
                .headers("Content-type","text/xml;charset=UTF-8")
                .body("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.gatewaymg.tigo.com.co/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <web:uninstallSubscription>\n" +
                        "         <!--Optional:-->\n" +
                        "         <arg0>\n" +
                        "            <!--Optional:-->\n" +
                        "            <msisdn>"+MSISDN+"</msisdn>\n" +
                        "            <!--Optional:-->\n" +
                        "            <transactionId>?</transactionId>\n" +
                        "         </arg0>\n" +
                        "      </web:uninstallSubscription>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>")
                .post(URL)
                .then().statusCode(200)
                .extract().asString();
        return response;
    }

    /**ejecuta el servicio para eliminar la cuenta asociada a la linea*/
    public String acctDeactivationGatewayCBS(String account, String URL){
        String response = given()
                .headers("Content-type","text/xml;charset=UTF-8")
                .body("<soapenv:Envelope xmlns:bcc=\"http://www.huawei.com/bme/cbsinterface/bccommon\" xmlns:bcs=\"http://www.huawei.com/bme/cbsinterface/bcservices\" xmlns:ws=\"http://ws.web.gatewaycbs.tigo.com.co/\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "<soapenv:Header/>\n" +
                        "<soapenv:Body>\n" +
                        "<ws:acctDeactivation>\n" +
                        "<!--Optional:-->\n" +
                        "<AcctDeactivationRequest>\n" +
                        "<bcs:AcctAccessCode>\n" +
                        "<bcc:AccountKey>"+account+"</bcc:AccountKey>\n" +
                        "</bcs:AcctAccessCode>\n" +
                        "<bcs:OpType>2</bcs:OpType>\n" +
                        "</AcctDeactivationRequest>\n" +
                        "</ws:acctDeactivation>\n" +
                        "</soapenv:Body>\n" +
                        "</soapenv:Envelope>\n")
                .post(URL)
                .then().statusCode(200)
                .extract().asString();
        return response;
    }

    /**Permite extraer informacion del response por medio de la llave o leabel del response ejemplo: ns3:Acctkey
       internamente el busca la llave que abre como la que cierra y trae la informacion que tiene*/
    public String extractResponseInformation(String response, String llave ){
        String buscarLlaveInicio= "<"+llave+">";
        String buscarLlaveFinal = "</"+llave+">";
        int posicion1= response.indexOf(buscarLlaveInicio)+buscarLlaveInicio.length();
        int posicion2= response.indexOf(buscarLlaveFinal);
        String AcctKey=response.substring(posicion1,posicion2).replace("PRE-","");
        return AcctKey;
    }

    /** realiza la validación, si el response muestra el mensaje de Operation successfully.*/
    public boolean validateActiveAccount(String response){
        if(extractResponseInformation(response,"ns5:ResultDesc").equals("Operation successfully.")) {
            return true;
        }
        return false;
    }

    /**  Realiza la validación, si la cantidad de accounts en el response es mayor 1 es diferente a prepago */
    public boolean validatesIfPlanIsDifferentToPrepaid(String response){
        if(countAccounts(response)>1) {
            return true;
        }
        return false;
    }

    /** Permite contar la cantidad de accounts en el response  */
    public int countAccounts(String response){
        int posicion=0;
        int contadorPalabras=0;
        String palabra = "<ns3:Account>";// busca el label para poder definir si es postago o prepago
        posicion = response.indexOf(palabra);
        while (posicion!=-1){
            contadorPalabras++;
            posicion=response.indexOf(palabra,posicion+1);
        }
        return contadorPalabras;

    }

}