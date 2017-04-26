# Printing Service Wrapper

This is a stupid starting point: System.out.println is my friend in exploring Liferay's API.

This particular thing got started when reading [this forum post](https://web.liferay.com/community/forums/-/message_boards/view_message/87968373), where the original author did not want to have top level pages - instead every toplevel page should be a link to its first subpage. In order to enforce this, we could work quite a bit on the UI level, or just identify what happens on the API layer.   

Knowing that a page's name in the backend is "Layout", it's easy to identify the method responsible for creating a new Layout, by just looking at [LayoutLocalService](https://docs.liferay.com/portal/7.0-latest/javadocs/portal-kernel/com/liferay/portal/kernel/service/LayoutLocalService.html)'s interface: addLayout. There are three methods with that name, and a quick ServiceWrapper, injecting System.out.println does wonders in helping to identify what's happening on the API layer. 

