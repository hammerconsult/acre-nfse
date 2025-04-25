(function () {
    'use strict';

angular.module('nfseApp')
    .directive('searchBox', function ($translate, $locale, tmhDynamicLocale) {
        var tmplt = "<div class='form-group input-group'>\
                            <input type='text' class='form-control'\
                                 ng-model='ngModel' id='searchQuery'\
                                 ng-keyup='keyPressPlanItem($event)'\
                                 placeholder='Pesquisar ...'>\
                                    <span class='input-group-btn'>\
                                        <button class='btn btn-primary' ng-click='search()'><span\
                                             class='glyphicon glyphicon-search'></span>\
                                        </button>\
                                    </span>\
                    </div>";

        return {
            restrict: 'E',
            scope: {
                ngModel: '=',
                searchMethod: '&searchMethod'
            },
            require: '?ngModel',
            template: tmplt,
            replace: true,
            link: function (scope, elm, attrs, ctrl) {
                scope.search = function () {
                    scope.searchMethod();
                }
                scope.keyPressPlanItem = function ($event) {
                    if ($event.keyCode == 13) {
                        scope.search();
                    }
                };

            }
        };
    });

})();