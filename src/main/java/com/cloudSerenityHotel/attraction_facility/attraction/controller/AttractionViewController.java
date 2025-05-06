//package com.cloudSerenityHotel.attraction_facility.attraction.controller;
//
//import com.cloudSerenityHotel.attraction_facility.attraction.service.AttractionService;
//import com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import java.util.List;
//
//@Controller
//@RequestMapping("/attractions")
//public class AttractionViewController {
//
//    @Autowired
//    private AttractionService attractionService;
//
//    // 讓前台的 /FrontAttraction.html 顯示
//    @GetMapping("/frontend")
//    public String showFrontendAttractions(Model model) {
//        List<Attraction> attractions = attractionService.getAllAttractions();
//        model.addAttribute("attractions", attractions);
//        return "static/attraction/FrontAttraction";  // 這會對應到 FrontAttraction.html
//    }
//
//    // 讓後台的 /GetAllAttractions.html 顯示
//    @GetMapping("/backend")
//    public String showBackendAttractions(Model model) {
//        List<Attraction> attractions = attractionService.getAllAttractions();
//        model.addAttribute("attractions", attractions);
//        return "static/attraction/GetAllAttractions";  // 這會對應到 GetAllAttractions.html
//    }
//}
