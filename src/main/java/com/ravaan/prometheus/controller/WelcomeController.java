/**
 * @#WelcomeController.java October 24, 2018
 */
package com.ravaan.prometheus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class WelcomeController.
 */
@RestController
public class WelcomeController {

	/**
	 * Gets the welcome text.
	 *
	 * @return the welcome text
	 */
	@GetMapping(value = "/welcomeText")
	public String getWelcomeText() {
		return "Welcome, This is first application to monitor through Prometheus";
	}
}
