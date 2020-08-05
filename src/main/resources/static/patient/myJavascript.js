function confirmDeleteInSearch() {
                var conf = confirm("Ви впернені, що хочете видалити запис?");
                if (conf) {
                    document.getElementById("yesAnswer").checked = true;
                    document.getElementById("deleteForm").action = "/patient/delete";
                } else {
                    document.getElementById("yesAnswer").checked = false;
                    document.getElementById("deleteForm").action = "/patient/delete";
                }
            }
function confirmDeleteInSuccess() {
                var conf = confirm("Ви впернені, що хочете видалити запис?");
                if (conf) {
                    document.getElementById("yesAnswer").checked = true;
                    document.getElementById("deleteForm").action = "/patient/addNew";
                } else {
                    document.getElementById("yesAnswer").checked = false;
                    document.getElementById("deleteForm").action = "/patient/success";
                }
            }