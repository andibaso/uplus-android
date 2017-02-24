package com.pega.ubank.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public final class DummyData {
    private DummyData() {
        // hidden
    }

    public static List<DataModel> getAccountListData() {
        List<DataModel> list = new ArrayList<>();
        list.add(new HeaderModel().setName("Savings"));
        list.add(new AccountModel().setName("Ultimate Savings Account")
                                                            .setDescription("On deposit").setCurrency("$")
                                                            .setAmount(923.81f));
        list.add(new AccountModel().setName("Savings Plus Account")
                                                            .setDescription("On deposit").setCurrency("$")
                                                            .setAmount(1122.43f));
        list.add(new HeaderModel().setName("Lines and Loans"));
        list.add(new AccountModel().setName("Checking Plus")
                                                            .setDescription("Amount you owe").setCurrency("$")
                                                            .setAmount(0.00f));
        list.add(new HeaderModel().setName("Credit Cards"));
        list.add(new AccountModel().setName("UPlus® Divident World")
                                                            .setDescription("Current balance").setCurrency("$")
                                                            .setAmount(1219.74f));
        return list;
    }

    public static List<DataModel> getAccountDetailsData(DetailedAmountModel detailedAmountModel) {
        List<DataModel> list = new ArrayList<>();
        if (detailedAmountModel != null) {
            list.add(detailedAmountModel);
        }
        if (detailedAmountModel != null && detailedAmountModel.isCC()) {
            list.add(new TransactionModel().setDescription("Available credit for purchases")
                                           .setCurrency("$")
                                           .setAmount(7878.00f));
            list.add(new TransactionModel().setDescription("Minimum Payment Due on 06/03/2017")
                                           .setCurrency("$")
                                           .setAmount(65.27f));
            list.add(new TransactionModel().setDescription("Rewards Earned in Last Statement")
                                           .setCurrency("$")
                                           .setAmount(231.27f));

            list.add(new HeaderModel().setName("CREDIT CARD ACTIVITY"));
            list.add(new TransactionModel().setDate(new GregorianCalendar(2016, 4, 30).getTime())
                                           .setDescription("WESTFIELD CO OF WI\n608-836-1945 WI")
                                           .setCurrency("$")
                                           .setAmount(157.25f));
            list.add(new TransactionModel().setDate(new GregorianCalendar(2016, 4, 30).getTime())
                                           .setDescription(
                                                   "DOMINO'S 1802\n543-653-4656 CAM")
                                           .setCurrency("$")
                                           .setAmount(24.37f).setStatus("Pending"));
            list.add(new TransactionModel().setDate(new GregorianCalendar(2016, 4, 29).getTime())
                                           .setDescription(
                                                   "TST* COOK\nRESTAURANT ARLIN")
                                           .setCurrency("$")
                                           .setAmount(24.37f));
            list.add(new TransactionModel().setDate(new GregorianCalendar(2016, 4, 29).getTime())
                                           .setDescription(
                                                   "BALTIMORE SUN\nSUBSCRIP 888539126549987347")
                                           .setCurrency("$")
                                           .setAmount(9.93f));
        }
        return list;
    }

    public static List<DataModel> getTransactionDetailsData(DetailedAmountModel detailedAmountModel, String header,
                                                            Date date) {
        List<DataModel> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
        if (header != null) {
            list.add(new HeaderModel().setName(header));
        }
        if (detailedAmountModel != null) {
            list.add(detailedAmountModel);
        }
        list.add(new TransactionDetailModel().setKey("Date").setValue(dateFormat.format(date)));
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        list.add(new TransactionDetailModel().setKey("Date posted").setValue(dateFormat.format(calendar.getTime())));
        return list;
    }
}
