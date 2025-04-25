(function () {
    'use strict';

angular.module('nfseApp')
    .directive('currencyInput', function ($browser, $filter) {
        return {
            require: 'ngModel',
            link: function ($scope, $element, $attrs, ngModel) {
                var listener = function () {
                    var value = $element.val().replace(/,/g, '');
                    $element.val($filter('number')(value, 0));
                };

                // This runs when we update the text field from element
                ngModel.$parsers.push(function (viewValue) {
                    return parseInt(viewValue.replace(/,/g, ''), 10);
                });

                // This runs when the model gets updated on the scope directly and keeps our view in sync
                ngModel.$formatters.push(function (modelValue) {
                    return modelValue == null || modelValue.length === 0 ? '' : $filter('number')(modelValue, 0);
                });

                ngModel.$viewChangeListeners = function () {
                    $element.val($filter('number')(ngModel.$viewValue, 0))
                }

                $element.bind('change', listener);
                $element.bind('keypress', function (event) {
                    var key = event.which;
                    // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                    // This lets us support copy and paste too
                    if (key === 0 || key === 8 || (15 < key && key < 19) || (37 <= key && key <= 40)) {
                        return;
                    }
                    $browser.defer(listener); // Have to do this or changes don't get picked up properly
                });
                $element.bind('paste cut', function () {
                    $browser.defer(listener);
                });
            }
        }
    });

})();