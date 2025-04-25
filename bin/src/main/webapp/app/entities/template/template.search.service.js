(function () {
    'use strict';

angular.module('nfseApp')
    .factory('TemplateSearch', function ($resource) {
        return $resource('api/_search/templates/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
})();
