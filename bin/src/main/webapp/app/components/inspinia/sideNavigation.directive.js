(function () {
    'use strict';

    angular.module('nfseApp')
        .directive('sideNavigation', function ($timeout) {
            return {
                restrict: 'A',
                link: function(scope, element) {
                    // Call the metsiMenu plugin and plug it to sidebar navigation
                    $timeout(function(){
                        element.metisMenu();

                    });
                }
            };
        });

})();