package com.indra.steps_definitions;

import com.indra.actions.ReadFileXLSX;
import com.indra.actions.UninstallCBSServices;
import com.indra.models.dataExcel;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public class Prueba {


    @Managed
    WebDriver driver;
    UninstallCBSServices uninstallCBSServices = new UninstallCBSServices(driver);
    ReadFileXLSX fileXLSX = new ReadFileXLSX();
    dataExcel dataExcel = new dataExcel();

    @Given("^estoy probando$")
    public void estoyProbando() {
        uninstallCBSServices.performLineCleaning(dataExcel.getUrlGatewayCBS(), dataExcel.getUrlGatewayCBS(), dataExcel.getMSISDN());
        System.out.println("este es el numero "+ dataExcel.getMSISDN() +" que se encuentra en el archivo de excel");
    }

    @When("^hago algo$")
    public void hagoAlgo() {

    }

    @Then("^deberia poder ver lo que pasa$")
    public void deberiaPoderVerLoQuePasa() {

    }

}
