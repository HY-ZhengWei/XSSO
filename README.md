XSSO

统一单点登陆验证服务



1. 可对单点登陆验证服务也进行集群。其目的是为了安全，多一个容灾用的备份服务，并不是为了提高性能，它也没有多大的性能消耗，所以集群两台即可。

2. 在实现应用中，需将应用服务的"会话用户"类放在本项目中。可以打成jar包的方式，或直接复制源码。