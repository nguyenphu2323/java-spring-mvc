<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Order Update ${id}</title>
                    <!-- Latest compiled and minified CSS -->
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <!-- Latest compiled JavaScript -->
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <link href="/css/styles.css" rel="stylesheet" />
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
                    <script>
                        $(document).ready(() => {
                            const avatarFile = $("#avatarFile");
                            avatarFile.change(function (e) {
                                const imgURL = URL.createObjectURL(e.target.files[0]);
                                $("#avatarPreview").attr("src", imgURL);
                                $("#avatarPreview").css({ "display": "block" });
                            });
                        });
                    </script>
                </head>

                <body>

                    <jsp:include page="../layout/header.jsp" />

                    <div id="layoutSidenav">
                        <jsp:include page="../layout/sidebar.jsp" />
                        <div id="layoutSidenav_content">
                            <main>
                                <div class="container-fluid px-4">
                                    <h1 class="mt-4">Manage Order</h1>
                                    <ol class="breadcrumb mb-4">
                                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                        <li class="breadcrumb-item"><a href="/admin/order">Order</a></li>

                                        <li class="breadcrumb-item active">Update Order</li>
                                    </ol>
                                    <div class="mt-5">
                                        <div class="row">
                                            <div class="col-12 mx-auto">
                                                <!-- Tiêu đề và thông tin id, price -->
                                                <h3>Update a order</h3>
                                                <p>Order id = ${id} &nbsp;&nbsp; Price:
                                                    <fmt:formatNumber type="number" value="${newOrder.totalPrice}" />đ
                                                </p>
                                                <hr>

                                                <!-- Form cập nhật -->
                                                <form:form modelAttribute="newOrder" method="post"
                                                    action="/admin/order/update">
                                                    <!-- Input ẩn cho ID -->
                                                    <div class="d-none">
                                                        <form:input type="text" class="form-control" path="id"
                                                            value="${id}" />
                                                    </div>

                                                    <!-- User -->
                                                    <div class="row mb-3">
                                                        <div class="col-2">
                                                            <label class="form-label">User:</label>
                                                        </div>
                                                        <div class="col-10">
                                                            <input class="form-control" type="text"
                                                                value="${newOrder.user.fullName}"
                                                                aria-label="Disabled input example" disabled readonly>
                                                        </div>
                                                    </div>

                                                    <!-- Status -->
                                                    <div class="row mb-3">
                                                        <div class="col-2">
                                                            <label class="form-label">Status:</label>
                                                        </div>
                                                        <div class="col-10">
                                                            <form:select path="status" class="form-select">
                                                                <form:option value="PENDING">PENDING</form:option>
                                                                <form:option value="SHIPPING">SHIPPING</form:option>
                                                                <form:option value="COMPLETE">COMPLETE</form:option>
                                                                <form:option value="CANCEL">CANCEL</form:option>
                                                            </form:select>
                                                        </div>
                                                    </div>

                                                    <!-- Nút Update -->
                                                    <button class="btn btn-warning">UPDATE</button>
                                                </form:form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </main>
                            <jsp:include page="../layout/footer.jsp" />
                        </div>
                    </div>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                        crossorigin="anonymous"></script>
                    <script src="js/scripts.js"></script>

                </body>

                </html>