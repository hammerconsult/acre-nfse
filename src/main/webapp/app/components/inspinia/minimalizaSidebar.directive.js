(function () {
    'use strict';


    var minimalizaSidebar = {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 text-white" href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: minimalizaSidebarController
    }

    angular
        .module('nfseApp')
        .component('minimalizaSidebar', minimalizaSidebar);

    minimalizaSidebarController.$inject = ['$scope'];


    function minimalizaSidebarController($scope) {

        $scope.minimalize = function () {
            $("body").toggleClass("mini-navbar");
            if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                // Hide menu in order to smoothly turn on when maximize menu
                $('#side-menu').hide();
                // For smoothly turn on menu
                setTimeout(
                    function () {
                        $('#side-menu').fadeIn(500);
                    }, 100);
            } else if ($('body').hasClass('fixed-sidebar')) {
                $('#side-menu').hide();
                setTimeout(
                    function () {
                        $('#side-menu').fadeIn(500);
                    }, 300);
            } else {
                // Remove all inline style from jquery fadeIn function to reset menu state
                $('#side-menu').removeAttr('style');
            }
        }

    };
})();


