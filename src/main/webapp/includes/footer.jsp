
<%
    String footerContext = request.getContextPath();
%>

<footer class="bb-footer">
    <div class="bb-footer-container">

        <div class="bb-footer-brand">
            <a class="bb-footer-logo"
               href="<%= footerContext %>/restaurant">
                Bite<span>Box</span>
            </a>

            <p>
                Good food, great moments.
                Your next favourite meal is just a few clicks away.
            </p>
        </div>

        <div class="bb-footer-column">
            <h3>Explore</h3>
            <a href="<%= footerContext %>/restaurant">Restaurants</a>
            <a href="<%= footerContext %>/cart">Your Cart</a>
            <a href="<%= footerContext %>/orders">Your Orders</a>
        </div>

        <div class="bb-footer-column">
            <h3>Account</h3>
            <a href="<%= footerContext %>/login">Login</a>
            <a href="<%= footerContext %>/register">Create Account</a>
        </div>

    </div>

    <div class="bb-footer-bottom">
        <p>
            &copy; <%= java.time.Year.now() %> BiteBox.
            All rights reserved.
        </p>
        <span>Made with care for food lovers.</span>
    </div>
</footer>
