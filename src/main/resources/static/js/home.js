var valid = false;
function validate_fileupload(input_element) {
    var el = document.getElementById("error");
    console.log(el);
    var elem = document.getElementById("labelForFile");
    var allowed_extensions = 'xlsx';
    var fileName = input_element.split("\\").pop();
    var file_extension = input_element.split('.').pop();
    if (input_element === "") {
        elem.innerHTML = 'Chose a XLSX file'
    } else {
        elem.innerHTML = fileName;
    }
    if (allowed_extensions === file_extension.toLowerCase()) {
        valid = true;
        el.innerHTML = "";
        return;
    }
    el.innerHTML = "Please select a valid XLSX file.";
    valid = false;
}
function valid_form() {
    return valid;
}
