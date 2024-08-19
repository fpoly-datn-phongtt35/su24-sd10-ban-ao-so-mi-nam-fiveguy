app.controller("SizeController", function($scope, $http){
    $scope.page = 0;
    $scope.num = 5;

    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.size = {};
    $scope.sizeUpdate = {};
    $scope.sizes = [];
    
    $scope.getAllSizes = () => {
        if ($scope.num <= 0 || !Number.isInteger($scope.num)) {
            $scope.num = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/size`, 
                {params: {page: $scope.page, size: $scope.num, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.sizes = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchSizes = () => {
        $scope.page = 0;
        $scope.getAllSizes();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllSizes();
        }
    }

    $scope.getAllSizes();

    $scope.resetForm = () => {
        $scope.size = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.sizeUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editSize = (key) => {
        $http.get(`${config.host}/size/${key}`).then((response) => {
            $scope.sizeUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.size = element;
    }

    $scope.handleStatus = (element) => {
        $scope.size = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/size/status/${$scope.size.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllSizes();
            $scope.size = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            console.log("Error", error);
        })
    }

    $scope.createSize = () => {
        if ($scope.sizeForm.$valid) {
            $('#addSize').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/size`, $scope.size).then((response) => {
                $('#addSize').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addSizeModel').modal('hide');
                $scope.getAllSizes();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addSize').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateSize = () => {
        if ($scope.sizeFormUpdate.$valid) {
            $('#updateSize').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/size/${$scope.sizeUpdate.id}`, $scope.sizeUpdate).then(response => {
                $('#updateSize').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editSizeModal').modal('hide');
                $scope.getAllSizes();
                $scope.resetFormUpdate();
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateSize').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteSize').css('display', 'none');
        $('#loadingSize').css('display', 'inline-block');
        $http.delete(`${config.host}/size/${$scope.size.id}`).then(response => {
            $('#deleteSize').css('display', 'inline-block');
            $('#loadingSize').css('display', 'none');
            $('#deleteSizeModel').modal('hide');
            $scope.getAllSizes();
            $scope.size = {};
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteSize').css('display', 'inline-block');
            $('#loadingSize').css('display', 'none');
            toastr["error"](error);
        })
    }


})