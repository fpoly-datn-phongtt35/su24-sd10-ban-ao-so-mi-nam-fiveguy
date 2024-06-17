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
        $http.get(`${config.host}/material`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.materials = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
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
        $http.put(`${config.host}/material/status/${$scope.material.id}`).then(response => {
            $scope.getAllMaterials();
            $scope.material = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createMaterial = () => {
        if ($scope.materialForm.$valid) {
           $http.post(`${config.host}/material`, $scope.material).then((response) => {
                $scope.getAllMaterials();
                $scope.resetForm();
                $('#addMaterialModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateMaterial = () => {
        if ($scope.materialFormUpdate.$valid) {
            $http.put(`${config.host}/material/${$scope.materialUpdate.id}`, $scope.materialUpdate).then(response => {
                $scope.getAllMaterials();
                $scope.resetFormUpdate();
                $('#editMaterialModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/material/${$scope.material.id}`).then(response => {
            $scope.getAllMaterials();
            $scope.material = {};
            $('#deleteMaterialModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})