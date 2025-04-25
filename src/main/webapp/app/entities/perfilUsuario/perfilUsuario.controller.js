(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SettingsController', function ($state, $scope, $modal, $timeout, entity, Principal, Pessoa, CEP, Municipio, Notificacao, Imagem) {


        $scope.pessoa = entity;

        $scope.myCroppedImage = "";

        $scope.carregarImagemUsuario = function () {

            Imagem.getImageFromUsuario({}, function (imagem) {
                console.log('imagem do usuario', imagem)
                if (imagem && imagem.conteudo) {
                    $scope.imagemUsuario = imagem.conteudo;
                    $scope.idImagem = imagem.id;
                }
            });
        }

        $scope.carregarImagemUsuario();

        $scope.loadEnderecoByCEP = function (cep) {
            if (cep) {
                CEP.getByCep({cep: cep}, function (endereco) {
                    $scope.pessoa.dadosPessoais.cep = endereco.cep;
                    $scope.pessoa.dadosPessoais.municipio = endereco.municipio;
                    $scope.pessoa.dadosPessoais.logradouro = endereco.logradouro;
                    $scope.pessoa.dadosPessoais.bairro = endereco.bairro;
                });
            }
        };

        $scope.loadMunicipioByCodigo = function (codigo) {
            if (codigo) {
                Municipio.getByCodigo({codigo: codigo}, function (municipio) {
                    if (municipio.id) {
                        $scope.pessoa.dadosPessoais.municipio = municipio;
                    } else {
                        Notificacao.warn("Atenção", "Não foi encotrado nenhum Municipio com o código " + codigo);
                        $scope.pessoa.dadosPessoais.municipio = {};
                    }
                });
            }
        };

        $scope.searchMunicipio = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/entities/municipio/municipio-search.html',
                controller: 'MunicipioSearchController',
                size: 'lg'
            });
            modalInstance.result.then(function (municipio) {
                $scope.pessoa.dadosPessoais.municipio = municipio;
            }, function () {
                //$log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.save = function () {

            Pessoa.save($scope.pessoa, onSuccess, onError);

            function onSuccess(data) {
                $state.reload();

                if ($scope.myImage && $scope.myCroppedImage) {
                    var imagem = {conteudo: $scope.myCroppedImage, pessoa: data};
                    Imagem.updateImagemUsuario(imagem, function (data) {
                        Notificacao.priority("Configurações salvas com sucesso!");
                        $scope.$emit('nfseApp:navbarUpdate', data);
                        location.reload(true);
                    });
                } else {
                    Notificacao.priority("Configurações salvas com sucesso!");
                    $scope.$emit('nfseApp:navbarUpdate', data);
                    location.reload(true);
                }
            };
            function onError() {
                Notificacao.error("Ocorreu um erro!", "As configurações não foram salvas.");
            };
        };


        $scope.upload = function (file) {
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function ($scope) {
                    $scope.myImage = evt.target.result;
                });
            };
            reader.readAsDataURL(file);
        };

        $scope.delete = function () {
            Imagem.deleteImagemUsuario({id: $scope.idImagem}, function(response) {
                location.reload(true);
            });
        };

        $scope.cancelButton = function () {
            $scope.carregarImagemUsuario();
            $scope.myImage = null;
        }
    });
})();