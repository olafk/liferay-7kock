# API Exploration

I've wanted to resurrect 7cogs from the dead for a while. 7cogs is still known to those Liferay-Old-Timers and used to be a hook that - programmatically - set up a full demo site, including users, sites, pages, content. 

There are better and more elegant ways in Liferay to do these things without scripting, but 7cogs had one advantage: Being a stupidly simple script that runs from top to bottom, it was actual executable documentation that you could read interactively in the debugger. 

Liferay APIs can be intimidating, requiring 10 or more parameters, and not all of them are trivial. This project, and especially the 'ApiExplorationPortlet' is rather meant as work in progress, I've been using it to explore API simplifications and interactively call them - e.g. programmatically create a page with a single parameter, defaulting all the others. This won't be everything you need, but it's a starting point: Some times you'll just need different parameters than the default. This is not a problem at all. This project also has some portlets that should help you find some information about API level configuration (like 'PageInformationPortlet', 'PortletInformationPortlet')

As I've also used [Alfredo's implementation of the RandomUser API](https://github.com/adelcastillo/addUsers), this project currently is depending on 'gson' for JSON parsing. For compile-time, it should easily resolve. In order to deploy this project to Liferay, you'll need to deploy gson as well. Luckily it's an OSGi bundle already and you just need to download and drop it in Liferay's deploy folder.

