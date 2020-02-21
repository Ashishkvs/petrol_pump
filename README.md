temp1.map(function (e1){ return e1.dayNo;});
@stackoverflow.com/questions/31117260/how-to-get-a-list-of-key-values-from-array-of-objects-javascript

#Date Selector enable and disabled date for upcoming dates
#Multiple Nozzle updater
#image of totalizer updater
#petrol pump location sharer
#validate today's data with previous days data
#error page configuration
https://www.thymeleaf.org/doc/articles/springsecurity.html

#favicon error
https://www.baeldung.com/spring-boot-favicon

@ToString(exclude = "nozzles")
#PersistentBag.toString() stackoverflow error while querying the

#TERNARy operator 
 str.equals("Ashish") ? "ASHISH" : false
 
 <expression> ? <if true> : <if false>
 
 #forward url without changing browser url
 @GetMapping("/login/{id}")
	public ModelAndView uiLogin(@PathVariable("id")Long pumpId,ModelMap modelMap) {
		Optional<Pump> pumpById = pumpRepository.findById(pumpId);
		modelMap.addAttribute("pumpById", pumpById);
		return new ModelAndView("forward:/ui/dashboard", modelMap);
	}
	
#detached entity issue
https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data