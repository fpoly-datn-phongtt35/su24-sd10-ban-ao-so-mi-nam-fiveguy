app.controller("CollarController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;

    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.collar = {};
    $scope.collarUpdate = {};
    $scope.collars = [];
    
    $scope.getAllCollars = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $http.get(`${config.host}/collar`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.collars = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                console.log("Error", error)
            })
    }

    $scope.searchCollars = () => {
        $scope.page = 0;
        $scope.getAllCollars();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllCollars();
        }
    }

    $scope.getAllCollars();

    $scope.resetForm = () => {
        $scope.collar = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.collarUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editCollar = (key) => {
        $http.get(`${config.host}/collar/${key}`).then((response) => {
            $scope.collarUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.collar = element;
    }

    $scope.handleStatus = (element) => {
        $scope.collar = element;
    }

    $scope.updateStatus = () => {
        $http.put(`${config.host}/collar/status/${$scope.collar.id}`).then(response => {
            $scope.getAllCollars();
            $scope.collar = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createCollar = () => {
        if ($scope.collarForm.$valid) {
           $http.post(`${config.host}/collar`, $scope.collar).then((response) => {
                $scope.getAllCollars();
                $scope.resetForm();
                $('#addCollarModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateCollar = () => {
        if ($scope.collarFormUpdate.$valid) {
            $http.put(`${config.host}/collar/${$scope.collarUpdate.id}`, $scope.collarUpdate).then(response => {
                $scope.getAllCollars();
                $scope.resetFormUpdate();
                $('#editCollarModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/collar/${$scope.collar.id}`).then(response => {
            $scope.getAllCollars();
            $scope.collar = {};
            $('#deleteCollarModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})