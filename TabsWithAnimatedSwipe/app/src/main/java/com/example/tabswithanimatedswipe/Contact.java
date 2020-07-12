package com.example.tabswithanimatedswipe;

public class Contact implements Comparable<Contact> {
    private String id; // id value
    private String name; // name of a person
    private String telNum; // telephone number of that person
    /* private Uri thumbNail; // Uri for thumbnail image */

    public Contact(String name, String telNum) {
        this.name = name;
        this.telNum = telNum;
    }

    public Contact() {
        this.name = "홍길동";
        this.telNum = "010-1234-5678";
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        Contact contact = (Contact) o;
        if(name.equals(contact.getName()) && telNum.equals(contact.getTelNum()))
            result = true;
        return result;
    }

    @Override
    public int compareTo(Contact contact) {

        return this.name.compareTo(contact.name);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }
    /*
    public void setThumbNail(Uri thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getId() {
        return this.id;
    }
    */
    public String getName() {
        return this.name;
    }

    public String getTelNum() {
        return this.telNum;
    }
    /*
    public Uri getThumbNail() {
        return this.thumbNail;
    }
     */
}