# GitHub에서 APK 만들기

1. GitHub에서 New repository를 누르고 이름을 `bedtime-player`로 지정합니다. Public 또는 Private 모두 가능합니다.
2. 압축파일을 풀고 `BedtimePlayer` 폴더 **안에 있는 파일과 폴더** (`.github`, `app`, `build.gradle`, `settings.gradle`, `gradle.properties` 등)를 새 저장소의 루트에 업로드합니다. `.github` 폴더도 반드시 포함합니다.
3. Commit changes를 누릅니다.
4. 저장소의 Actions 탭에서 `Build Android APK`를 선택합니다.
5. Run workflow 버튼을 눌러 실행합니다 (업로드 직후 자동 실행되었을 수도 있습니다).
6. 완료된 실행을 열고 Artifacts에서 `BedtimePlayer-APK`를 내려받아 압축을 풉니다.
7. `app-debug.apk`를 태블릿에 전송해 설치합니다. 필요시 Android 설정에서 해당 파일 관리 앱에 '알 수 없는 앱 설치'를 허용합니다.

이 프로젝트는 삼성 뮤직을 조작하지 않는 독립적인 로컬 오디오 플레이어입니다.
