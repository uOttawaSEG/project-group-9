# OTAMS

## Git Setup
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

