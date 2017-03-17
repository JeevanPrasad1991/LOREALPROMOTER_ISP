package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by rishikaa on 10-05-2016.
 */
public class PayslipGetterSetter {
    String emp_salary_table;
    ArrayList<String> SALARY_YEAR = new ArrayList<String>();
    ArrayList<String> ECODE = new ArrayList<String>();

    public ArrayList<String> getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH.add(MONTH);
    }

    ArrayList<String> MONTH = new ArrayList<String>();

    public ArrayList<String> getSALARY_YEAR() {
        return SALARY_YEAR;
    }

    public void setSALARY_YEAR(String SALARY_YEAR) {
        this.SALARY_YEAR.add(SALARY_YEAR);
    }

    public ArrayList<String> getECODE() {
        return ECODE;
    }

    public void setECODE(String ECODE) {
        this.ECODE.add(ECODE);
    }

    public ArrayList<String> getEMP_NAME() {
        return EMP_NAME;
    }

    public void setEMP_NAME(String EMP_NAME) {
        this.EMP_NAME.add(EMP_NAME);
    }

    public ArrayList<String> getPAYMENT_MODE() {
        return PAYMENT_MODE;
    }

    public void setPAYMENT_MODE(String PAYMENT_MODE) {

        this.PAYMENT_MODE.add(PAYMENT_MODE);
    }

    public ArrayList<String> getPRESENT_DAYS() {
        return PRESENT_DAYS;
    }

    public void setPRESENT_DAYS(String PRESENT_DAYS) {
        this.PRESENT_DAYS.add(PRESENT_DAYS);
    }

    public ArrayList<String> getINCENTIVE() {
        return INCENTIVE;
    }

    public void setINCENTIVE(String INCENTIVE) {
        this.INCENTIVE.add(INCENTIVE);
    }

    public ArrayList<String> getTOTAL_EARNING() {
        return TOTAL_EARNING;
    }

    public void setTOTAL_EARNING(String TOTAL_EARNING) {
        this.TOTAL_EARNING.add(TOTAL_EARNING);
    }

    public ArrayList<String> getPF() {
        return PF;
    }

    public void setPF(String PF) {
        this.PF.add(PF);
    }

    public ArrayList<String> getESI() {
        return ESI;
    }

    public void setESI(String ESI) {
        this.ESI.add(ESI);
    }

    public ArrayList<String> getPT() {
        return PT;
    }

    public void setPT(String PT) {
        this.PT.add(PT);
    }

    ArrayList<String> EMP_NAME = new ArrayList<String>();
    ArrayList<String> PAYMENT_MODE = new ArrayList<String>();
    ArrayList<String> PRESENT_DAYS = new ArrayList<String>();
    ArrayList<String> INCENTIVE = new ArrayList<String>();
    ArrayList<String> TOTAL_EARNING = new ArrayList<String>();

    ArrayList<String> PF = new ArrayList<String>();
    ArrayList<String> ESI = new ArrayList<String>();

    ArrayList<String> PT = new ArrayList<String>();
    ArrayList<String> LWF = new ArrayList<String>();
    ArrayList<String> MIS_DEDUCTION = new ArrayList<String>();

    ArrayList<String> NATIONAL_H = new ArrayList<String>();

    public ArrayList<String> getLWF() {
        return LWF;
    }

    public void setLWF(String LWF) {
        this.LWF.add(LWF);
    }

    public ArrayList<String> getMIS_DEDUCTION() {
        return MIS_DEDUCTION;
    }

    public void setMIS_DEDUCTION(String MIS_DEDUCTION) {
        this.MIS_DEDUCTION.add(MIS_DEDUCTION);
    }

    public ArrayList<String> getTDS() {
        return TDS;
    }

    public void setTDS(String TDS) {
        this.TDS.add(TDS);
    }

    public ArrayList<String> getTAKE_HOME() {
        return TAKE_HOME;
    }

    public void setTAKE_HOME(String TAKE_HOME) {
        this.TAKE_HOME.add(TAKE_HOME);
    }

    ArrayList<String> TDS = new ArrayList<String>();
    ArrayList<String> TAKE_HOME = new ArrayList<String>();


    public String getEmp_salary_table() {
        return emp_salary_table;
    }

    public void setEmp_salary_table(String emp_salary_table) {
        this.emp_salary_table = emp_salary_table;
    }


    public ArrayList<String> getNATIONAL_H() {
        return NATIONAL_H;
    }

    public void setNATIONAL_H(String NATIONAL_H) {
        this.NATIONAL_H.add(NATIONAL_H);
    }
}
