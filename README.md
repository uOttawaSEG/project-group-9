# OTAMS

#### Clone the repository
```
git clone git@github.com:uOttawaSEG/project-group-9.git

cd project-group-9
```

#### Verify the remote
```
git remote -v
# You should see "origin  git@github.com:uOttawaSEG/project-group-9.git (fetch)" and "(push)"
```

#### Fetch everything and switch to `dev`
```
git fetch origin
git checkout -t origin/dev    # creates local 'dev' tracking 'origin/dev'
```

## Development

#### Create your own feature branch from `dev`
For each task (e.g., developing the login page, implementing the database, updating code, or fixing bugs), create a new branch from `dev`. First switch to `dev` and update it, then branch out:
```
git branch -v # Assuming you are on main
git pull # Pull latest changes

git checkout dev # Switch to dev
git pull # Pull latest changes

git checkout -b <yourname>/<feature-name>
# Examples: `onur/login-ui`
git push -u origin <yourname>/<feature-name>

```
#### Make frequent commits with clear messages
```
git branch -v # Make sure you are on your own branch <yourname>/<short-feature-name>
git add <updated files...>
git commit -m "Concise summary of change"
git push
```
## Pull Request (PR)
Now that the feature is complete, it’s time to merge. We created our own branch for the task, committed changes with concise messages, and pushed them to the remote. Now open a pull request to merge your feature branch into `dev`.

My onur/README.md has updates
<img width="1565" height="1091" alt="1" src="https://github.com/user-attachments/assets/a8f11522-2c13-4f48-b40a-22c905bc9161" />
Let's switch to onur/README.md
<img width="1565" height="1101" alt="2" src="https://github.com/user-attachments/assets/ee79ba1d-1d9f-4b48-92c1-1addc90793c2" />
Open PR
<img width="1706" height="1116" alt="3" src="https://github.com/user-attachments/assets/5a18a512-0817-4f9a-ae51-43201361b5ec" />
**Make sure you are merging into dev, not main!**
<img width="2532" height="1266" alt="5" src="https://github.com/user-attachments/assets/78c1c354-ece0-45d4-b307-2c2d1231a155" />
<img width="2521" height="1236" alt="6" src="https://github.com/user-attachments/assets/db610890-4d6e-4df0-9a58-b1f4f861d300" />
<img width="2525" height="1215" alt="7" src="https://github.com/user-attachments/assets/83d75ee9-3ac6-4f81-931a-70bfe91a82de" />
After your pull request is merged into `dev`, you may delete your feature branch and begin the process again for the next task.
<img width="1407" height="1107" alt="8" src="https://github.com/user-attachments/assets/82a64aef-b55f-4392-b8b5-c951a193c7bf" />

