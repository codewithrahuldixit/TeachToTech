document.addEventListener('DOMContentLoaded', function () {

    const editNotesForm = document.getElementById('editNotesForm');
    const addContentFieldBtn = document.getElementById('addContentField');
    const topicSelect = document.getElementById("topic");
    const newTopicField = document.getElementById("newTopicField");
    const categorySelect = document.getElementById("category");
    const addTopicBtn = document.getElementById("addTopicBtn");
    const newTopicNameInput = document.getElementById("newTopic");


    addTopicBtn.addEventListener('click',
        /**
         * Add new topic to the topic list
         * @param event
         * @returns {Promise<void>}
         * @description This function is used to add a new topic to the topic list
         * @event click This function is called when the addTopicBtn is clicked
         */
        function (event) {
            console.debug('addTopicBtn clicked');

            let newTopicName = newTopicNameInput.value
            let selectedCategory = categorySelect.value;

            console.debug(newTopicName);
            console.debug(selectedCategory);

            let newTopic = {
                name: newTopicName,
                category: {
                    categoryId: selectedCategory
                }
            };

            // Fetch API to create a new topic
            fetch('/topic/createTopic', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newTopic)
            })
                .then(response => {
                    if (!response.ok) {
                        // Handle HTTP errors (e.g., 404, 500)
                        console.error('HTTP error! Status:', response.status);
                        return response.text().then(text => {
                            throw new Error(text)
                        }); // throw error with text response.
                    }
                    return response.json(); // Or response.text() if it's not JSON

                })

                .then(data => {
                    console.debug("=======================")
                    console.debug('API Response:', data);
                    let newOption = document.createElement('option')
                    newOption.value = data.topicId;
                    newOption.textContent = data.name;
                    console.debug("newOption: ", newOption)
                    topicSelect.appendChild(newOption);
                    topicSelect.value = data.topicId;
                    newTopicField.style.display = "none";
                    newTopicName.value = "";

                    newTopicField.classList.replace("d-block", "d-none");
                    categorySelect.removeAttribute("required");
                    const topicSavedToast = document.getElementById('topicSavedToast');
                    topicSavedToast.classList.replace('d-none', 'd-block');
                    setTimeout(() => {
                        topicSavedToast.classList.replace('d-block', 'd-none');
                    }, 3000);
                })
                .catch(
                    error => {
                        console.error('Error:', error);
                    }
                )
            newTopicField.classList.replace("d-block", "d-none");
            categorySelect.removeAttribute("required");
        });

    topicSelect.addEventListener('change', function (event) {
        if (topicSelect.value === "newTopic") {
            newTopicField.classList.replace("d-none", "d-block");
            categorySelect.setAttribute("required", "");
        } else {
            newTopicField.classList.replace("d-block", "d-none");
            categorySelect.removeAttribute("required");
        }
    });

    addContentFieldBtn.addEventListener('click', function (event) {
        console.log('addMoreContent with EvenListener');
        const contentFields = document.getElementById('contentFields');
        const contentField = document.createElement('textarea');
        contentField.classList.add('form-control');
        contentField.classList.add('mt-2');
        contentField.setAttribute('name', 'content[]');
        contentFields.appendChild(contentField);
    });

    editNotesForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const formData = new FormData(editNotesForm)
        let updatedContent = formData.getAll("content[]");
        let note = {
            "noteId": topic.note.noteId,
            "content": updatedContent,
            "topic": topic
        }
        console.debug('note:', note);

        fetch('/api/topic/edit/' + note.noteId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(note)
        })
            .then(response => {
                if (!response.ok) {
                    // Handle HTTP errors (e.g., 404, 500)
                    console.error('HTTP error! Status:', response.status);
                    return response.text().then(text => {
                        throw new Error(text)
                    }); // throw error with text response.
                }
                return response.text(); // Or response.text() if it's not JSON

            })
            .then(data => {
                console.debug('API Response:', data);
                const noteUpdatedToast = document.getElementById('noteUpdatedToast');
                noteUpdatedToast.classList.replace('d-none', 'd-block');
                setTimeout(() => {
                    noteUpdatedToast.classList.replace('d-block', 'd-none');
                }, 3000);
            })
            .catch(
                error => {
                    console.error('Error:', error);
                }
            )
    });
});
