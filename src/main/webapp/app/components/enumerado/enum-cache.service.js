(function () {
    'use strict';

    angular
        .module('nfseApp')
        .factory('EnumCacheService', EnumCacheService);

    EnumCacheService.$inject = ['EnumeradoService', '$q'];

    function EnumCacheService(EnumeradoService, $timeout) {

        var service = {
            carregarValuesEnum: carregarValuesEnum,
            buscarLabelEnum: buscarLabelEnum
        };

        var cache = [];

        function carregarValuesEnum(nameEnum) {
            var values = undefined;
            angular.forEach(cache, function (item) {
                if (item.nameEnum == nameEnum) {
                    values = item.values;
                }
            });
            if (values == undefined) {
                values = EnumeradoService.values(nameEnum);
                cache.push({'nameEnum': nameEnum, 'values': values});
            }
            return values;
        }

        function buscarLabelEnum(name, nameEnum) {
            var label = name;
            var values = carregarValuesEnum(nameEnum);
            angular.forEach(values, function (data) {
                if (data.name == name) {
                    label = data.value;
                }
            });
            return label;
        }

        return service;
    }
})();
