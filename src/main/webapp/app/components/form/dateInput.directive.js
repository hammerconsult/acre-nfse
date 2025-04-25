(function () {
    'use strict';

angular.module('nfseApp')
    .directive('dateInput', function ($translate, $locale, tmhDynamicLocale) {
        var tmplt = "<p class='input-group {{class}} date-input'> \
            <input type='text' class='form-control '\
              datepicker-popup='dd/MM/yyyy'\
              ng-readonly='readonly'\
              ng-model='ngModel' is-open='dateDialogOpened'\
             /> \
            <span class='input-group-btn'> \
                <button type='button' class='btn btn-info' ng-click='openDateDialog($event)'> \
                    <i class='glyphicon glyphicon-calendar'></i> \
                </button> \
            </span> \
        </p>";

        return {
            restrict: 'E',
            scope: {
                ngModel: '=',
                readonly: '='
            },
            require: '?ngModel',
            template: tmplt,
            replace: true,
            link: function (scope, elm, attrs, ctrl) {
                scope.class = attrs.class;
                scope.dateDialogOpened = false;
                scope.openDateDialog = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    scope.dateDialogOpened = true;
                };
            }
        };
    });

})();