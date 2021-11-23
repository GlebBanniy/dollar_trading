package com.dollartrading.trading.controllers;

import com.dollartrading.trading.models.Bid;
import com.dollartrading.trading.repos.BidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestController {
    @Autowired
    private BidRepo bidRepo;


    @GetMapping("/")
    public void test(Model model){



    }

}
