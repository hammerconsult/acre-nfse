(function () {
    'use strict';

angular.module('nfseApp')
    .directive('searchBoxCpf', function ($translate, $locale, tmhDynamicLocale) {
        var tmplt = "\
        <div class='form-group' ng-form='formSearchBoxCpf'> \
        CPF\
        <div class='input-group' >\
                            <input type='text' class='form-control' name='searchByCpf' \
                             ng-model='ngModel'\
                             ui-br-cpf-mask='' \
                             required\
                                 ng-keyup='keyPressPlanItem($event)'\
                                 placeholder='{{&quot;global.form.search&quot; | translate}}'>\
                                    <span class='input-group-btn'>\
                                        <button class='btn btn-info'  ng-click='search()' ng-disabled='formSearchBoxCpf.searchByCpf.$invalid'>\
                                         <span class='glyphicon glyphicon-search'></span>\
                                        </button>\
                                    </span>\
                                    </div>\
       <div class='has-error' ng-show='formSearchBoxCpf.searchByCpf.$invalid'>\
       <p class='help-block small' ng-show='formSearchBoxCpf.searchByCpf.$error.required'>\
            O CPF é obrigatório.\
        </p>\
        <p class='help-block small' ng-show='formSearchBoxCpf.searchByCpf.$invalid' >\
            O CPF é inválido.\
        </p>  \
        </div>\
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
                };

                scope.keyPressPlanItem = function ($event) {
                    if ($event.keyCode == 13) {
                        if (scope.formSearchBoxCpf.searchByCpf.$valid) {
                            scope.search();
                        }
                    }
                };

                scope.$watch('searchByCpf.$valid', function () {
                });

            }
        };
    });

})();