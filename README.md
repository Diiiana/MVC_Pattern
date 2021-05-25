# MVC_Pattern
MVC Design Pattern for a Cake Shops management system using Java and JavaFx.
Using NoSQL database for persistence - db.xml is the file containing data for users, cakes, cakeshops.

Complex class diagram, use case with all its functions, activity and sequence diagrams.

Roles: admin and user. Admin can add to a specific cake shop, view, update, delete users. User can view, add to its workplace (cake shop), delete from it, update a cake (especially its quantity), download cake reports - JSON or CSV - if selected a cake shop - download its report.

Model - contains all model classes - Cake, CakeShop, User, etc.
Controller - contains the operations associated which each object from the user interface.
View - contains only fxml files which create user interface.
