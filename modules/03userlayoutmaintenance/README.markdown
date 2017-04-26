# User Layout Maintenance

After exploring Liferay's API a bit and remembering 7cogs, a problem struck me:

In Liferay, a user can have personal sites. They're probably best managed through UserGroups and their templates, but this is also kind of limited. If you need to be more dynamic than that, you can also maintain user sites individually. Of course, if you have more than 5 users, you don't want to maintain their individual sites manually. Instead, you'll automate this task: Dynamically select the proper users, and "attack" their personal sites through the API. 

Go ahead, hard code exactly what you need. You've got my blessing.

In this project, I did exactly that. And yes, the starting point of code duplicates some other code. There's a big *To Do* in here: Extract all the API simplification calls into its own service. This is an activity that's already started, but just not (yet?) in this project.