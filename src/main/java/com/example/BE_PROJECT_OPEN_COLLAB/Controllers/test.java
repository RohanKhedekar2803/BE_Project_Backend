package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class test {

	@GetMapping("test")
	public String test() {
		return "heyy good morning ";
	}

	@GetMapping("/")
	public String test1() {
		return "hi";
	}
}
