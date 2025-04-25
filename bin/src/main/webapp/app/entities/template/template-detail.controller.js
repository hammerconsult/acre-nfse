(function () {
    'use strict';

angular.module('nfseApp')
    .controller('TemplateDetailController', function ($sce, $scope, $rootScope, $stateParams, entity, Template) {
        $scope.template = entity;
        $scope.load = function (id) {
            Template.get({id: id}, function(result) {
                $scope.template = result;
                $scope.conteudoTemplate = $sce.trustAsHtml(template.template);
            });
        };

        $rootScope.$on('nfseApp:templateUpdate', function(event, result) {
            $scope.template = result;
        });
    });
})();
