let valid = false;
function validate_fileupload(input_element) {
    let el = document.getElementById("error");
    let elem = document.getElementById("labelForFile");
    let allowed_extensions = 'xlsx';
    let fileName = input_element.split("\\").pop();
    let file_extension = input_element.split('.').pop();
    if (input_element === "") {
        elem.innerHTML = 'Alege&#355;i un fi&#351;ier XLSX'
    } else {
        elem.innerHTML = fileName;
    }
    if (allowed_extensions === file_extension.toLowerCase()) {
        valid = true;
        el.innerHTML = "";
        return;
    }
    el.innerHTML = "Alege&#355;i un fi&#351;ier XLSX valid!";
    valid = false;
}
function valid_form() {
    return valid;
}
