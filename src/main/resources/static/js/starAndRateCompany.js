document.addEventListener("DOMContentLoaded", function () {
    // Select all elements with class 'open-rating' and attach event listener to each
    document.querySelectorAll('.open-rating').forEach(function (button) {
        button.addEventListener('click', function () {
            // Find the closest rating widget overlay relative to the clicked button
            var overlay = this.closest('.content-box').querySelector('.rating-widget-overlay');
            if (overlay) {
                overlay.style.display = 'block';
            }
        });
    });

    // Select all elements with class 'close-rating' and attach event listener to each
    document.querySelectorAll('.close-rating').forEach(function (button) {
        button.addEventListener('click', function () {
            // Find the closest rating widget overlay relative to the clicked button
            var overlay = this.closest('.rating-widget-overlay');
            if (overlay) {
                overlay.style.display = 'none';
            }
        });
    });



    var ratingTextElements = document.querySelectorAll('.price');

    ratingTextElements.forEach(function (ratingTextElement) {
        var ratingText = ratingTextElement.textContent;
        var rating = parseInt(ratingText);

        const fullStarSrc = '/media/icons8-star-50.png';
        const emptyStarSrc = '/media/icons8-star-50gray.png';

        ratingTextElement.textContent = '';

        var ratingContainer = ratingTextElement;

        if (rating === 0) {
            for (var i = 0; i === 5; i++) {
                var starDiv = document.createElement('div');
                starDiv.style.display = 'inline-block';
                starDiv.style.width = '28px';
                starDiv.style.height = '20px';
                var starImg = document.createElement('img');
                starImg.src = emptyStarSrc;
                starDiv.appendChild(starImg);
                ratingContainer.appendChild(starDiv);
            }
        }

        for (var i = 0; i < Math.floor(rating); i++) {
            var starDiv = document.createElement('div');
            starDiv.style.display = 'inline-block';
            starDiv.style.width = '28px';
            starDiv.style.height = '20px';
            var starImg = document.createElement('img');
            starImg.src = fullStarSrc;
            starDiv.appendChild(starImg);
            ratingContainer.appendChild(starDiv);
        }

        for (var i = Math.floor(rating); i < 5; i++) {
            var starDiv = document.createElement('div');
            starDiv.style.display = 'inline-block';
            starDiv.style.width = '28px';
            starDiv.style.height = '20px';
            var starImg = document.createElement('img');
            starImg.src = emptyStarSrc;
            starDiv.appendChild(starImg);
            ratingContainer.appendChild(starDiv);
        }
    });
});