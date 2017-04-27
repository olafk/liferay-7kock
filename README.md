# 7kock
Resurrection of Liferay's old 7cogs code as an API tutorial. Experimental code!

If you're aware of Liferay's old [Sevencogs](https://web.liferay.com/de/web/james.falkner/blog/-/blogs/7cogs-is-dead-long-live-7cogs-) sample code, you'll remember that it used the API to build a full demo site - from creating user accounts over sites, pages and content. 

As trivial as this kind of code is, IMHO the main value of this code was its executability: You can easily run it in a debugger and check what's actually getting passed into the Liferay API when all of those tasks are executed. This, in turn, enabled a great understanding of the API.

Quite often, relevant methods in the Liferay API have more than 10 (or even more than 20) parameters, many of which often just utilize default values. The purpose of this code is to resurrect the old usefulness of simplified API notation - e.g. in order to create a page, we'll need a name and nothing else. As default values, this implementation will utilize a hardcoded layout, only create "portlet" type pages and choose other default values.

If you need more: Use this project for exploring the API and then actually use it. 

You may use this project as a learning environment, a starting point to explore Liferay's API. If you believe that you found something useful for others - please send a pullrequest. Please make sure to include as much documentation (answering the "why" question: Why did you create this code? What was the problem you wanted to solve? What other options do you see / did you omit / will you explore next / are related to this? 

Code with no explanation will be rejected. Go as far as possible: This code is not meant as production code, but as a resource to learn - the most important part of this code is its documentation.
