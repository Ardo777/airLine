// Template Name: FlyNow
// Template URL: https://techpedia.co.uk/template/flyNow
// Description:  FlyNow - Flight Booking Html Template
// Version: 1.0.0

(function (window, document, $, undefined) {
  "use strict";
  var Init = {
    i: function (e) {
      Init.s();
      Init.methods();
    },
    s: function (e) {
      (this._window = $(window)),
        (this._document = $(document)),
        (this._body = $("body")),
        (this._html = $("html"));
    },
    methods: function (e) {
      Init.w();
      Init.BackToTop();
      Init.preloader();
      Init.hamburgerMenu();      
      Init.tilt();
      Init.availableTags();
      Init.header();
      Init.searchToggle();
      Init.billingAddress();
      Init.quantityHandle();
      Init.passengerBox();
      Init.roomBox();
      Init.achivementCountdown();
      Init.counterActivate();
      Init.timepicker();
      Init.filterToggle();
      // Init.redirectNextPage();
      Init.wizardInit();
      Init.initializeSlick();
      Init.formValidation();
      Init.contactForm();
      Init.jsSlider();
      Init.videoPlay();
      // Init.wizardStepValidator();
      Init.VideoPlayer();
      Init.salInit();
      // Init.backToLogin();
    },

    w: function (e) {
      this._window.on("load", Init.l).on("scroll", Init.res);
    },
    // Back To Top
    BackToTop: function () {
      var btn = $("#backto-top");
      $(window).on("scroll", function () {
        if ($(window).scrollTop() > 300) {
          btn.addClass("show");
        } else {
          btn.removeClass("show");
        }
      });
      btn.on("click", function (e) {
        e.preventDefault();
        $("html, body").animate(
          {
            scrollTop: 0,
          },
          "300"
        );
      });
    },
    // Preloader
    preloader: function () {
      setTimeout(function () {
        $("#preloader").hide("slow");
      }, 2000);
    },

      // Ham Burger Menu
    hamburgerMenu: function () {
      if ($(".hamburger-menu").length) {
        $('.hamburger-menu').on('click', function() {
          $('.bar').toggleClass('animate');
          $('.mobile-navar').toggleClass('active');
          return false;
        })
        $('.has-children').on ('click', function() {
          $(this).children('ul').slideToggle('slow', 'swing');
          $('.icon-arrow').toggleClass('open');
        });
      }
    },
    // Tilt
    tilt: function () {
      let educateTiltElm = $(".flynow-tilt");
      if (educateTiltElm.length) {
        educateTiltElm.each(function () {
          let self = $(this);
          let options = self.data("tilt-options");
          let educateTilt = self.tilt(
            "object" === typeof options ? options : JSON.parse(options)
          );
        });
      }
    },
      // Header 
      header: function () {
        function dynamicCurrentMenuClass(selector) {
          let FileName = window.location.href.split("/").reverse()[0];
  
          selector.find("li").each(function () {
            let anchor = $(this).find("a");
            if ($(anchor).attr("href") == FileName) {
              $(this).addClass("current");
            }
          });
          selector.children("li").each(function () {
            if ($(this).find(".current").length) {
              $(this).addClass("current");
            }
          });
          if ("" == FileName) {
            selector.find("li").eq(0).addClass("current");
          }
        }
  
        if ($(".main-menu__list").length) {
          let mainNavUL = $(".main-menu__list");
          dynamicCurrentMenuClass(mainNavUL);
        }
  
        if ($(".main-menu__nav").length && $(".mobile-nav__container").length) {
          let navContent = document.querySelector(".main-menu__nav").innerHTML;
          let mobileNavContainer = document.querySelector(".mobile-nav__container");
          mobileNavContainer.innerHTML = navContent;
        }
        if ($(".sticky-header__content").length) {
          let navContent = document.querySelector(".main-menu").innerHTML;
          let mobileNavContainer = document.querySelector(".sticky-header__content");
          mobileNavContainer.innerHTML = navContent;
        }
  
        if ($(".mobile-nav__container .main-menu__list").length) {
          let dropdownAnchor = $(
            ".mobile-nav__container .main-menu__list .dropdown > a"
          );
          dropdownAnchor.each(function () {
            let self = $(this);
            let toggleBtn = document.createElement("BUTTON");
            toggleBtn.setAttribute("aria-label", "dropdown toggler");
            toggleBtn.innerHTML = "<i class='fa fa-angle-down'></i>";
            self.append(function () {
              return toggleBtn;
            });
            self.find("button").on("click", function (e) {
              e.preventDefault();
              let self = $(this);
              self.toggleClass("expanded");
              self.parent().toggleClass("expanded");
              self.parent().parent().children("ul").slideToggle();
            });
          });
        }
  
        if ($(".mobile-nav__toggler").length) {
          $(".mobile-nav__toggler").on("click", function (e) {
            e.preventDefault();
            $(".mobile-nav__wrapper").toggleClass("expanded");
            $("body").toggleClass("locked");
          });
        }
  
        $(window).on("scroll", function () {
          if ($(".stricked-menu").length) {
            var headerScrollPos = 130;
            var stricky = $(".stricked-menu");
            if ($(window).scrollTop() > headerScrollPos) {
              stricky.addClass("stricky-fixed");
            } else if ($(this).scrollTop() <= headerScrollPos) {
              stricky.removeClass("stricky-fixed");
            }
          }
        });
      },
  
     // Search Toggle 
    searchToggle: function () {
      if ($(".search-toggler").length) {
        $(".search-toggler").on("click", function (e) {
          e.preventDefault();
          $(".search-popup").toggleClass("active");
          $(".mobile-nav__wrapper").removeClass("expanded");
          $("body").toggleClass("locked");
        });
      }
    },

    billingAddress: function () {
      if ($(".input-control").length) {
        // $('.billing-address-form').hide();
        $('.input-control').change(function() {
            if ($(this).is(':checked')) {
                $('.v-2').hide("slow");
            } else {
                $('.v-2').show("slow");
            }
        });
      }
    },

    // redirectNextPage: function (){
    //   $(document).ready(function () {
    //     $('#nextPage').on('click', function () {
    //         window.location.href = 'flight-booking.html#step-2';
    //     });
    // });
    // },
    passengerBox: function () {
      if($('.pessenger-box').length){
      var $div = $('.pessenger-box');

  
      $(document).on('click', function(event) {
        var isClickInsideDiv = $div.is(event.target) || $div.has(event.target).length > 0;
        var isClickInsideButton = $(event.target).is('.seat-booking');
  
        if (!isClickInsideDiv && !isClickInsideButton) {
          $div.hide('slow');
        }
      });

        $('.seat-booking').on('click',function(){
          $('.pessenger-box').show('slow');
        })

        $('.increment').on('click',function(){
          var adult = +$('#adult').val();
          var child = +$('#child').val();
          var infant = +$('#infant').val();
          
          var total = adult+child+infant;

          $('.total-pasenger').text(total)
        })
        $('.decrement').on('click',function(){
          var adult = +$('#adult').val();
          var child = +$('#child').val();
          var infant = +$('#infant').val();
          
          var total = adult+child+infant;

          $('.total-pasenger').text(total)
        })
        $('.radio-button').on('click',function(){
          var $this = $(this).val();
          $('.pasenger-class').text($this);
        })
      }

    },
    roomBox: function () {
      var $div = $('.room-box');

  
      // Event listener to hide the div when clicking outside it
      $(document).on('click', function(event) {
        var isClickInsideDiv = $div.is(event.target) || $div.has(event.target).length > 0;
        var isClickInsideButton = $(event.target).is('.room-booking');
  
        if (!isClickInsideDiv && !isClickInsideButton) {
          $div.hide('slow');
        }
      });
      if($('.room-box').length){
        $('.room-booking').on('click',function(){
          $('.room-box').show('slow');
        })
        $('.increment').on('click',function(){
          var adult = +$('#adult').val();
          var child = +$('#child').val();
          var infant = +$('#infant').val();
          
          var total = adult+child+infant;

          $('.total-pasenger').text(total)
        })
        $('.decrement').on('click',function(){
          var adult = +$('#adult').val();
          var child = +$('#child').val();
          var infant = +$('#infant').val();
          
          var total = adult+child+infant;

          $('.total-pasenger').text(total)
        })
        $('.radio-button').on('click',function(){
          var $this = $(this).val();
          $('.pasenger-class').text($this);
        })
      }

    },
    jsSlider: function () {
      if ($(".js-slider").length) {
        $(".js-slider").ionRangeSlider({
          skin: "big",
          type: "double",
          grid: false,
          min: 0,
          max: 999,
          from: 0,
          to: 999,
          // postfix: '$',
          hide_min_max: true,
          hide_from_to: true,
        });
      }
    },
    quantityHandle: function () {
      $(".decrement").on("click", function () {
        var qtyInput = $(this).closest(".quantity-wrap").children(".number");
        var qtyVal = parseInt(qtyInput.val());
        if (qtyVal > 0) {
          qtyInput.val(qtyVal - 1);
        }
      });
      $(".increment").on("click", function () {
        var qtyInput = $(this).closest(".quantity-wrap").children(".number");
        var qtyVal = parseInt(qtyInput.val());
        qtyInput.val(parseInt(qtyVal + 1));
      });
    },
    initializeSlick: function (e) {
      if ($(".flight-card-slider").length) {
        $(".flight-card-slider").slick({
          slidesToShow: 4,
          slidesToScroll: 1,
          infinite: true,
          autoplaySpeed: 4000,
          arrows: true,
          dots: false,
          autoplay: true,
          centerPadding: "0",
          cssEase: "linear",
          responsive: [
            {
              breakpoint: 1599,
              settings: {
                slidesToShow: 3,
              },
            },
            {
              breakpoint: 1099,
              settings: {
                slidesToShow: 2,
              },
            },
            {
              breakpoint: 675,
              settings: {
                slidesToShow: 1,
              },
            },
            {
              breakpoint: 399,
              settings: {
                arrows: false,
                slidesToShow: 1,
              },
            },
          ],
        });
      }
      if ($(".hotel-image-slider").length) {
        $(".hotel-image-slider").slick({
          infinite: true,
          slidesToShow: 1,
          slidesToScroll: 1,
          arrows: false,
          dots: false,
          autoplay: true,
          autoplaySpeed: 4000,
          responsive: [
            
          ],
        });
      }
      if ($(".testimonial-slider").length) {
        $(".testimonial-slider").slick({
          infinite: true,
          autoplay: false,
          slidesToShow: 1,
          slidesToScroll: 1,
          arrows: true,
          dots: false,
          autoplaySpeed: 4000,
          responsive: [
            {
              breakpoint: 767,
              settings: {
                arrows: false,
                slidesToShow: 1,
              },
            },
          ],
        });
      }
    },
    wizardInit: function () {
      if ($("#form-wizard").length) {
        // $('#form-wizard').smartWizard();
      }
    },
    salInit: function () {
      sal({
        threshold: 0.1,
        once: true,
      });
    },

    // Achivement Counter 
    counterActivate:function() {
      $(".counter-count .count").each(function () {
        $(this)
          .prop("Counter", 0)
          .animate(
            {
              Counter: $(this).text()
            },
            {
              duration: 2000,
              easing: "swing",
              step: function (now) {
                $(this).text(Math.ceil(now), 3);
              }
            }
          );
      });
    },

    achivementCountdown:function() {
      var section = document.querySelector(".counter-section");
      var hasEntered = false;
      if (!section) return;
  
      var initAnimate = window.scrollY + window.innerHeight >= section.offsetTop;
      if (initAnimate && !hasEntered) {
        hasEntered = true;
        this.counterActivate();
      }
  
      window.addEventListener("scroll", (e) => {
        var shouldAnimate =
          window.scrollY + window.innerHeight >= section.offsetTop;
  
        if (shouldAnimate && !hasEntered) {
          hasEntered = true;
          this.counterActivate();
        }
      });
    },

    availableTags: function(){
      if($('.slector-wrapper').length){
        var availableTags = [
          "New York City",
          "Los Angeles",
          "Toronto ",
          "Paris",
          "London ",
          "Rome ",
          "Berlin",
          "Tokyo ",
          "Beijing ",
          "Mumbai ",
          "Dubai ",
          "Rio de Janeiro",
          "Sydney",
          "Melbourne",
          "Karachi ",
          "Lahore",
          "Islamabad",
          "Brisbane",
          "Cape Town ",
          "Marrakech ",
          "Delhi ",
          "Sharjah"
        ];
        $( ".auto-input" ).autocomplete({
          source: availableTags
        });
      }
      if($('.slector-wrapper').length){
        var availableTags = [
          "The Ritz-Carlton, Paris",
          "Burj Al Arab Jumeirah",
          "The Plaza Hotel ",
          "The Savoy",
          "Hotel de Glace ",
          "Marina Bay Sands ",
          "Atlantis The Palm",
          "The Waldorf Astoria ",
          "The Peninsula ",
          "Four Seasons Hotel George V  ",
          "Mandarin Oriental ",
          "Emirates Palace",
          "Hotel del Coronado",
          "The Beverly Hills Hotel",
          "Adlon Kempinski ",
          "The Langham",
          "The Shard - Shangri-La ",
          "The Breakers",
          "The Burj Khalifa - Armani Hotel ",
          "Hotel Okura ",
        ];
        $( ".auto-input-hotel" ).autocomplete({
          source: availableTags
        });
      }
      if($('.slector-wrapper').length){
        var availableTags = [
          "Beverly Hills - Los Angeles, USA",
          "Notting Hill - London, UK",
          "Harlem - New York City, USA ",
          "Shibuya - Tokyo, Japan",
          "Montmartre - Paris, France ",
          "Copacabana - Rio de Janeiro, Brazil",
          "Sydney's Rocks - Sydney, Australia",
          "Manhattan - New York City, USA",
          "Pudong - Shanghai, China",
          "Santa Monica - Los Angeles, USA",
          "Gion - Kyoto, Japan ",
          "Venice Beach - Los Angeles, USA",
          "The Rocks - Sydney, Australia",
          "Greenwich Village - New York City, USA",
          "Old Montreal - Montreal, Canada",
        ];
        $( ".auto-input-location" ).autocomplete({
          source: availableTags
        });
      }
      if($('.slector-wrapper').length){
        var availableTags = [
          "USD - $",
          "Euro - €",
          "Pound Sterling - £ ",
          "Yen - ¥",
          "Franc - ₣ ",
          "Dinar - د. ك",
          "Dirham Symbol - د. إ",
          "Rupee Symbol - ₹",
        ];
        $( ".auto-input-currency" ).autocomplete({
          source: availableTags
        });
      }
    },
    timepicker:function(){
      $(document).ready(function () {
        $('.timepicker').timepicker({
            timeFormat: 'h:mm p',
            interval: 60,
            minTime: '10',
            maxTime: '6:00pm',
            defaultTime: '11',
            startTime: '10:00',
            dynamic: false,
            dropdown: true,
            scrollbar: false
        });
    });

    },

    filterToggle: function(){
      if($('.filter-block').length){
        $(".filter-block .title").on("click", function (e) {
          var count = $(this).data('count');
          if($('.filter-block.box-'+count+' .content-block').is(':visible')){
            $('.filter-block.box-'+count+' i').removeClass('fal fa-chevron-up');
            $('.filter-block.box-'+count+' i').addClass('fal fa-chevron-down');
            $('.filter-block.box-'+count+' .content-block').hide('slow');

          }else{
            
            $('.filter-block.box-'+count+' i').removeClass('fal fa-chevron-down');
            $('.filter-block.box-'+count+' i').addClass('fal fa-chevron-up');
            $('.filter-block.box-'+count+' .content-block').show('slow');
          }
        })
      }

      if($('.filter-block-2').length){
        $(".filter-block-2 .title").on("click", function (e) {
          var count = $(this).data('count');
          if($('.filter-block-2.box-'+count+' .content-block').is(':visible')){
            $('.filter-block-2.box-'+count+' i').removeClass('fal fa-caret-up');
            $('.filter-block-2.box-'+count+' i').addClass('fas fa-caret-down');
            $('.filter-block-2.box-'+count+' .content-block').hide('slow');

          }else{
            
            $('.filter-block-2.box-'+count+' i').removeClass('fal fa-caret-down');
            $('.filter-block-2.box-'+count+' i').addClass('fal fa-caret-up');
            $('.filter-block-2.box-'+count+' .content-block').show('slow');
          }
        })
      }
    },

    
    formValidation: function () {
      if ($(".contact-form").length) {
        $(".contact-form").validate();
      }
    },

    contactForm: function () {
      $(".contact-form").on("submit", function (e) {
        e.preventDefault();
        if ($(".contact-form").valid()) {
          var _self = $(this);
          _self
            .closest("div")
            .find('button[type="submit"]')
            .attr("disabled", "disabled");
          var data = $(this).serialize();
          $.ajax({
            url: "./assets/mail/contact.php",
            type: "post",
            dataType: "json",
            data: data,
            success: function (data) {
              $(".contact-form").trigger("reset");
              _self.find('button[type="submit"]').removeAttr("disabled");
              if (data.success) {
                document.getElementById("message").innerHTML =
                  "<h5 class='text-success mt-3 mb-2'>Email Sent Successfully</h5>";
              } else {
                document.getElementById("message").innerHTML =
                  "<h5 class='text-danger mt-3 mb-2'>There is an error</h5>";
              }
              $("#message").show("slow");
              $("#message").slideDown("slow");
              setTimeout(function () {
                $("#message").slideUp("hide");
                $("#message").hide("slow");
              }, 3000);
            },
          });
        } else {
          return false;
        }
      });
    },
    VideoPlayerHeight: function () {
      if ($(".aks-vp-start").length) {
        $('.aks-vp-start').on('click', function () {
          $('.videoplayer').removeClass('no_variable_height');
          $('.videoplayer').addClass('variable_height','slow');
        })
      }
    },
    VideoPlayer: function () {
      if ($("#video").length) {
        var path = $('#video').attr('img-path')
        $("#video").aksVideoPlayer({
          file: [
              {
              file: "assets/media/videos/video-1080.mp4",
              label: "1080p"
              },
              {
              file: "assets/media/videos/video-720.mp4",
              label: "720p"
              },
              {
              file: "assets/media/videos/video-540.mp4",
              label: "540p"
              },
              {
              file: "assets/media/videos/video-360.mp4",
              label: "360p"
              },
              {
              file: "assets/media/videos/video-240.mp4",
              label: "240p"
              }
          ],
          poster: path,
          forward: true,
        });
      }
    },


//----------------- Wizard Validator -----------------//

    // wizardStepValidator: function () {
    //   $(document).ready(function () {
    //     $("#wizardValidator").validate({
    //       rules: {
    //         field1: "required",
    //         field2: "required",
    //         // Add rules for other fields in other steps
    //       },
    //       messages: {
    //         field1: "Please enter a value for Step 1",
    //         field2: "Please enter a value for Step 2",
    //         // Add messages for other fields in other steps
    //       }
    //     });
    //   });
      
    //   $(document).ready(function() {
    //     var currentStep = 0;
     
    //     $(".sw-btn-next").on("click", function() {
    //        if ($("#wizardValidator").valid()) {
    //           currentStep++;
    //           showStep(currentStep);
    //        }
    //     });
     
    //     $(".sw-btn-prev").on("click", function() {
    //        currentStep--;
    //        showStep(currentStep);
    //     });
     
    //     function showStep(step) {
    //        $("#step").hide();
    //        $("#step:eq(" + step + ")").show();
    //     }
    //  });
    // },

    videoPlay:function(){
      $('#videoModal').on('hidden.bs.modal', function () {
        $('#videoModal video').get(0).pause();
      });
      $("#closeVideoModalButton").click(function() {
        $("#videoModal").modal("hide");
      });
    },
    continueEmail: function () {
      $('#continue-email').on('click',function(){
        $('.hide-link').hide()
        $('.login-sec').show()
      })
    },
    backToLogin: function () {
      $('#backtologin').on('click',function(){
        $('.hide-form').hide()
        $('.hide-link').show()
      })
    },
  };
  Init.i();
})(window, document, jQuery);
