# Hierarchy enforcing Service Wrapper

This is the continuation project for 01printingservicewrapper - solving the problem stated in [this forum post](https://web.liferay.com/community/forums/-/message_boards/view_message/87968373), where the original author did not want to have top level pages - instead every toplevel page should be a link to its first subpage. In order to enforce this, we could work quite a bit on the UI level, or just identify what happens on the API layer.

Note that this is the quick solution to the problem - it *might* not work under certain circumstances - e.g. when you import data that implies a different structure. We might need more conditions to make sure the system behaves well under these circumstances, but at least you can see that the basic problem requires only a handful of actual API calls that will be easy to maintain on future upgrades.
