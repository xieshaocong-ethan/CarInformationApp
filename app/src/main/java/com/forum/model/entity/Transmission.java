package com.forum.model.entity;

public class Transmission {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transmission.Num
     *
     * @mbg.generated
     */
    private String num;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transmission.Abbreviation
     *
     * @mbg.generated
     */
    private String abbreviation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transmission.BlockNumber
     *
     * @mbg.generated
     */
    private String blocknumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column transmission.TransmissionType
     *
     * @mbg.generated
     */
    private String transmissiontype;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transmission.Num
     *
     * @return the value of transmission.Num
     *
     * @mbg.generated
     */
    public String getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transmission.Num
     *
     * @param num the value for transmission.Num
     *
     * @mbg.generated
     */
    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transmission.Abbreviation
     *
     * @return the value of transmission.Abbreviation
     *
     * @mbg.generated
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transmission.Abbreviation
     *
     * @param abbreviation the value for transmission.Abbreviation
     *
     * @mbg.generated
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation == null ? null : abbreviation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transmission.BlockNumber
     *
     * @return the value of transmission.BlockNumber
     *
     * @mbg.generated
     */
    public String getBlocknumber() {
        return blocknumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transmission.BlockNumber
     *
     * @param blocknumber the value for transmission.BlockNumber
     *
     * @mbg.generated
     */
    public void setBlocknumber(String blocknumber) {
        this.blocknumber = blocknumber == null ? null : blocknumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column transmission.TransmissionType
     *
     * @return the value of transmission.TransmissionType
     *
     * @mbg.generated
     */
    public String getTransmissiontype() {
        return transmissiontype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column transmission.TransmissionType
     *
     * @param transmissiontype the value for transmission.TransmissionType
     *
     * @mbg.generated
     */
    public void setTransmissiontype(String transmissiontype) {
        this.transmissiontype = transmissiontype == null ? null : transmissiontype.trim();
    }
}