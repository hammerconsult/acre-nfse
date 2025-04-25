(function () {
    'use strict';

    angular.module('nfseApp')
        .service('DateUtils', function ($filter) {
            this.convertLocaleDateToServer = function (date) {
                return date;
            };
            this.convertLocaleDateFromServer = function (date) {
                return date;
            };
            this.convertDateTimeFromServer = function (date) {
                return date;
            };
            this.temHora = function (data) {
                if (!data)
                    return false;
                if (!(data instanceof Date)) {
                    data = new Date(data);
                }
                var hours = data.getHours();
                var minutes = data.getMinutes();
                return hours > 0 || minutes > 0;
            };
            this.convertDateLong = function (date) {
                if (date == null)
                    return null;

                var dateString = $filter('date')(date, 'yyyy-MM-dd', '+0000');
                var split = dateString.split("-");
                var ano = parseInt(split[0]);
                var mes = parseInt(split[1]) - 1;
                var dia = parseInt(split[2]);

                return new Date(ano, mes, dia, 0, 0, 0, 0);
            }
        });
})();