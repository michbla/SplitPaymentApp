package com.example.splitpaymentapp.model;

import java.util.List;

public interface IReportGenerated {
    void onGenerated(List<UserBalance> ub, List<Payment> p);
}
