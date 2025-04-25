(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('EnumeradoJsonService', function ($http) {
            return {
                getEnumValues: function (name) {
                    var enuns = [];
                    $http.get('i18n/pt-br/' + name + '.json').success(function (data) {
                        angular.forEach(Object.keys(data), function (primeiroField) {
                            angular.forEach(Object.keys(data[primeiroField]), function (sugundoField) {
                                angular.forEach(Object.keys(data[primeiroField][sugundoField]), function (field) {
                                    enuns.push({name: field, value: data[primeiroField][sugundoField][field]})
                                });

                            });
                        });


                    });
                    return enuns;
                }
            }
        });
})();
