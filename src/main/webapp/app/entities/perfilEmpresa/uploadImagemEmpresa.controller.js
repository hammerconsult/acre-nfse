(function () {
    'use strict';

angular.module('nfseApp')
    .controller('UploadImagemEmpresaController', function ($scope, $state, $modalInstance, prestadorServicos, imagemPrestador,
                                                           Imagem, Notificacao) {

        $scope.myCroppedImage = "";
        $scope.prestadorServicos = prestadorServicos;
        $scope.imagemPrestador = imagemPrestador;

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.upload = function (file) {
            $scope.imagemPrestador = null
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function ($scope) {
                    $scope.myImage = evt.target.result;
                });
            };
            reader.readAsDataURL(file);
        };

        $scope.updateImagem = function () {

            var imagem = {conteudo: $scope.myCroppedImage, pessoa: $scope.prestadorServicos.pessoa};

            Imagem.updateImagem(imagem, onSuccess, onError);

            function onSuccess(data) {
                $modalInstance.close();
                $state.reload();
                Notificacao.priority("Upload da logo realizado com sucesso.");
                $scope.$emit('nfseApp:navbarUpdate', data);
            };

            function onError(data) {
                Notificacao.error("Ocorreu um erro!", data);
            };
        };

        $scope.deleteImagem = function () {
            Imagem.deleteImagemUsuario({id: $scope.prestadorServicos.pessoa.id}, function(response) {
                $state.reload();
                $scope.cancel();
            });
        };
    });
})();