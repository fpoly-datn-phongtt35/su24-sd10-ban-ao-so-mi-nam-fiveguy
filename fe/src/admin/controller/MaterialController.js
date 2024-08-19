app.controller("MaterialController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;

    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.material = {};
    $scope.materialUpdate = {};
    $scope.materials = [];
    
    $scope.getAllMaterials = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/material`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.materials = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchMaterials = () => {
        $scope.page = 0;
        $scope.getAllMaterials();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllMaterials();
        }
    }

    $scope.getAllMaterials();

    $scope.resetForm = () => {
        $scope.material = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.materialUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editMaterial = (key) => {
        $http.get(`${config.host}/material/${key}`).then((response) => {
            $scope.materialUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.material = element;
    }

    $scope.handleStatus = (element) => {
        $scope.material = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/material/status/${$scope.material.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllMaterials();
            $scope.material = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            console.log("Error", error);
        })
    }

    $scope.createMaterial = () => {
        if ($scope.materialForm.$valid) {
            $('#addMaterial').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/material`, $scope.material).then((response) => {
                $('#addMaterial').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addMaterialModel').modal('hide');
                $scope.getAllMaterials();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addMaterial').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateMaterial = () => {
        if ($scope.materialFormUpdate.$valid) {
            $('#updateMaterial').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/material/${$scope.materialUpdate.id}`, $scope.materialUpdate).then(response => {
                $('#updateMaterial').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editMaterialModal').modal('hide');
                $scope.getAllMaterials();
                $scope.resetFormUpdate();
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateMaterial').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteMaterial').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/material/${$scope.material.id}`).then(response => {
            $('#deleteMaterial').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteMaterialModel').modal('hide');
            $scope.getAllMaterials();
            $scope.material = {};
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteMaterial').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }


})