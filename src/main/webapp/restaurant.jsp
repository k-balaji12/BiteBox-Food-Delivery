
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.app.model.Restaurant" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>BiteBox | Good Food, Good Vibes</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/navbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/restaurant.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/footer.css">
</head>

<body>

<%@ include file="/includes/navbar.jsp" %>

<%
    List<Restaurant> restaurants =
        (List<Restaurant>) request.getAttribute("restaurants");
%>

<main>

    <!-- HERO SECTION -->
    <section class="hero">

        <div class="hero-content">

            <div class="tagline">
                GOOD FOOD • GOOD PEOPLE • BRIGHTER DAYS
            </div>

            <h1>
                Meals that<br>
                <span>Make Life Better</span>
            </h1>

            <p class="hero-description">
                Discover amazing restaurants and get your favorite
                meals delivered fresh to your doorstep.
            </p>

            <a class="order-button" href="#restaurants">
                Order Now →
            </a>

            <div class="highlights">

                <div class="highlight">
                    <span class="highlight-icon">✓</span>
                    Wide Variety
                </div>

                <div class="highlight">
                    <span class="highlight-icon">⚡</span>
                    Fast Delivery
                </div>

                <div class="highlight">
                    <span class="highlight-icon">♥</span>
                    Fresh &amp; Delicious
                </div>

            </div>
        </div>

        <!-- Rotating food image -->
        <div class="hero-image-wrap">
            <div class="hero-food-stage">
                <div class="hero-food-float">
                    <img
                        id="heroFood"
                        class="hero-food active"
                        src="${pageContext.request.contextPath}/images/biryani.png"
                        alt="Delicious biryani">
                </div>
            </div>
        </div>

    </section>


    <!-- RESTAURANTS SECTION -->
    <section class="restaurants-section" id="restaurants">

        <div class="section-heading">

            <div class="section-kicker">
                YOUR NEXT FAVORITE MEAL
            </div>

            <h2>Explore Our Restaurants</h2>

            <p>
                Discover delicious food from restaurants around Hyderabad.
            </p>

        </div>

        <div class="restaurant-grid">

            <%
                if (restaurants != null && !restaurants.isEmpty()) {
                    for (Restaurant r : restaurants) {
            %>

            <article class="restaurant-card">

                <img
                    class="restaurant-image"
                    src="<%= r.getImagePath() != null
                        && !r.getImagePath().trim().isEmpty()
                        ? r.getImagePath()
                        : request.getContextPath() + "/images/biryani.png" %>"
                    alt="Food from <%= r.getName() %>"
                    loading="lazy">

                <div class="card-content">

                    <div class="card-title-row">

                        <h3><%= r.getName() %></h3>

                        <span class="rating">
                            ★ <%= r.getRating() %>
                        </span>

                    </div>

                    <p class="cuisine">
                        <%= r.getCuisineType() %>
                    </p>

                    <div class="card-footer">

                        <span>
                            ◷ <%= r.getDeliveryTime() %> min
                        </span>

                        <span class="restaurant-address">
                            <%= r.getAddress() %>
                        </span>

                    </div>

                    <div class="card-actions">

                        <a class="view-menu-button"
                           href="${pageContext.request.contextPath}/menu?restaurantId=<%= r.getRestaurantId() %>">
                            View Menu →
                        </a>

                    </div>

                </div>

            </article>

            <%
                    }
                } else {
            %>

            <div class="empty-state">
                <h3>No restaurants available yet</h3>
                <p>
                    Please check your restaurant database and DAO query.
                </p>
            </div>

            <%
                }
            %>

        </div>

    </section>

</main>

<%@ include file="/includes/footer.jsp" %>


<!-- HERO FOOD IMAGE ROTATION -->
<script>
document.addEventListener("DOMContentLoaded", function () {

    const contextPath = "${pageContext.request.contextPath}";
    const heroFood = document.getElementById("heroFood");

    const foodImages = [
        {
            src: contextPath + "/images/biryani.png",
            alt: "Delicious biryani"
        },
        {
            src: contextPath + "/images/burger.png",
            alt: "Fresh burger"
        },
        {
            src: contextPath + "/images/noodles.png",
            alt: "Tasty noodles"
        },
        {
            src: contextPath + "/images/pizza1.png",
            alt: "Freshly prepared pizza"
        }
    ];

    const reducedMotion = window.matchMedia(
        "(prefers-reduced-motion: reduce)"
    );

    let currentIndex = 0;
    let rotationTimer = null;
    let transitionTimer = null;

    // Preload images to make transitions smoother.
    foodImages.forEach(function (food) {
        const image = new Image();
        image.src = food.src;
    });

    function stopRotation() {
        if (rotationTimer !== null) {
            clearInterval(rotationTimer);
            rotationTimer = null;
        }

        if (transitionTimer !== null) {
            clearTimeout(transitionTimer);
            transitionTimer = null;
        }
    }

    function showNextImage() {
        const nextIndex = (currentIndex + 1) % foodImages.length;
        const nextFood = foodImages[nextIndex];
        const nextImage = new Image();

        nextImage.onload = function () {
            heroFood.classList.remove("active");

            transitionTimer = setTimeout(function () {
                heroFood.src = nextImage.src;
                heroFood.alt = nextFood.alt;

                requestAnimationFrame(function () {
                    requestAnimationFrame(function () {
                        heroFood.classList.add("active");
                    });
                });

                currentIndex = nextIndex;
                transitionTimer = null;
            }, 650);
        };

        nextImage.onerror = function () {
            console.warn("Could not load hero image:", nextFood.src);
        };

        nextImage.src = nextFood.src;
    }

    function startRotation() {
        stopRotation();

        if (reducedMotion.matches) {
            heroFood.classList.add("active");
            return;
        }

        rotationTimer = setInterval(showNextImage, 4500);
    }

    function handleMotionPreference() {
        if (reducedMotion.matches) {
            stopRotation();
            heroFood.classList.add("active");
        } else {
            startRotation();
        }
    }

    if (reducedMotion.addEventListener) {
        reducedMotion.addEventListener("change", handleMotionPreference);
    } else {
        reducedMotion.addListener(handleMotionPreference);
    }

    startRotation();
});
</script>

</body>
</html>
