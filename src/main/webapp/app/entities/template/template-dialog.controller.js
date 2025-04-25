(function () {
    'use strict';

angular.module('nfseApp').controller('TemplateDialogController',
    ['$scope','$state', '$stateParams',  'entity', 'Template',"Notificacao",
        function($scope,$state, $stateParams, entity, Template, Notificacao) {

        $scope.template = entity;
        $scope.load = function(id) {
            Template.get({id : id}, function(result) {
                $scope.template = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('nfseApp:templateUpdate', result);
            $state.go("template");
            Notificacao.info("Template salvo com sucesso.");
        };

        $scope.save = function () {
            if ($scope.template.id != null) {
                Template.update($scope.template, onSaveFinished);
            } else {
                Template.save($scope.template, onSaveFinished);
            }
        };

        $scope.clear = function() {
            //$modalInstance.dismiss('cancel');
        };
}]);
})();
